#!/bin/bash

# Test Invalid Authorization Token

API_URL="http://52.91.2.250:8080/query"
INVALID_API_KEY="invalid-api-key"  # Use an invalid API key

# Make a POST request with an invalid Authorization token
response=$(curl -s -w "%{http_code}" -o response.json -X POST "$API_URL" \
  -H "Authorization: Bearer $INVALID_API_KEY" \
  -H "Content-Type: application/json" \
  -d 'Hello, how are you?')

# Check if the response code is 500
if [ "$response" -eq 500 ]; then
  echo "Test Passed: Invalid authorization token returned a 500 Internal Server Error response."
else
  echo "Test Failed: Expected 500 Internal Server Error, but got $response."
fi
