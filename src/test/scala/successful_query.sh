#!/bin/bash

# Test Successful Query

API_URL="http://52.91.2.250:8080/query"
API_KEY="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"

# Make a POST request
response=$(curl -s -w "%{http_code}" -o response.json -X POST "$API_URL" \
  -H "Authorization: Bearer $API_KEY" \
  -H "Content-Type: application/json" \
  -d 'Hello, how are you?')

# Check if the response code is 200 (OK)
if [ "$response" -eq 200 ]; then
  echo "Test Passed: Valid query returned a 200 OK response."
else
  echo "Test Failed: Expected 200 OK, but got $response."
fi
