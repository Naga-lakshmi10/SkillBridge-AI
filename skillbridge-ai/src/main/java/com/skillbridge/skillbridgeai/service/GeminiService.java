package com.skillbridge.skillbridgeai.service;

import com.skillbridge.skillbridgeai.config.GeminiConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class GeminiService {

    private final GeminiConfig geminiConfig;
    private final RestClient restClient;

    public GeminiService(GeminiConfig geminiConfig) {
        this.geminiConfig = geminiConfig;
        this.restClient = RestClient.create();
    }

    public String analyzeWithGemini(String prompt) {

        String requestBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(escapeJson(prompt));

        try {

            String response = restClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent")
                    .header("x-goog-api-key", geminiConfig.getApiKey())
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            return extractText(response);

        } catch (HttpClientErrorException.TooManyRequests e) {

            throw new GeminiQuotaException(
                    "Gemini API quota has been exceeded. Please try again later."
            );

        } catch (HttpClientErrorException e) {

            throw new GeminiApiException(
                    "Gemini API returned an error: " + e.getStatusCode()
            );

        } catch (GeminiQuotaException | GeminiApiException e) {

            throw e;

        } catch (Exception e) {

            throw new GeminiApiException(
                    "Could not connect to the Gemini API."
            );
        }
    }

    private String escapeJson(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    private String extractText(String response) {

        String marker = "\"text\":";

        int start = response.indexOf(marker);

        if (start == -1) {
            return "Gemini response could not be parsed.";
        }

        start += marker.length();

        while (start < response.length()
                && Character.isWhitespace(response.charAt(start))) {
            start++;
        }

        if (start >= response.length()
                || response.charAt(start) != '"') {
            return "Gemini response could not be parsed.";
        }

        start++;

        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = start; i < response.length(); i++) {

            char current = response.charAt(i);

            if (escaped) {

                switch (current) {
                    case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r');
                    case 't' -> result.append('\t');
                    case '"' -> result.append('"');
                    case '\\' -> result.append('\\');
                    default -> result.append(current);
                }

                escaped = false;

            } else if (current == '\\') {

                escaped = true;

            } else if (current == '"') {

                break;

            } else {

                result.append(current);
            }
        }

        return result.toString().trim();
    }
}