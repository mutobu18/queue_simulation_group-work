import requests

# URL of the Flask server
BASE_URL = 'http://127.0.0.1:5000/products'

# Data for the new product
product_data = {
    "name": "Smartphone",
    "description": "Latest model with 128GB storage",
    "price": 799.99
}

# Send POST request to add the product
response = requests.post(BASE_URL, json=product_data)

# Print the server response
if response.status_code == 201:
    print("Product added successfully:", response.json())
else:
    print("Failed to add product:", response.status_code, response.json())