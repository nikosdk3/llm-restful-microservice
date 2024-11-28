#!/bin/bash

# Test Response Structure

API_URL="http://52.91.2.250:8080/query"
API_KEY="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"  # Replace with a valid OpenAI API key

# Make a POST request with a valid Authorization token and a message
response=$(curl -s -w "%{http_code}" -o response.json -X POST "$API_URL" \
  -H "Authorization: Bearer $API_KEY" \
  -H "Content-Type: application/json" \
  -d '')

# Check the HTTP status code
http_code=$response
response_body=$(cat response.json)

# Check if the response code is 200 (OK)
if [ "$http_code" -eq 200 ]; then
  # Check if the response body is meaningful (non-empty)
  if [[ -n "$response_body" ]]; then
    echo "Test Passed: Received valid response for an empty string request."
  else
    echo "Test Failed: Received an empty or meaningless response for an empty string request."
  fi
else
  echo "Test Failed: Expected 200 OK, but got $http_code."
fi