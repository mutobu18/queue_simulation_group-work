from flask import Flask, request, jsonify
import json

app = Flask(__name__)

# Load products from the JSON file
def load_products():
    try:
        with open('products.json', 'r') as file:
            return json.load(file)
    except FileNotFoundError:
        return []

# Save products to the JSON file
def save_products(products):
    with open('products.json', 'w') as file:
        json.dump(products, file, indent=4)

# POST /products: Create a new product
@app.route('/products', methods=['POST'])
def create_product():
    product=request.get_json()

    # Validate the input data
    if not product or not 'name' in product or not 'description' in product or not 'price' in product:
        return jsonify({"error":"Bad Request"}),400

    # Print the received data for debugging
    print("Received product data:", product)

    # Add the product to the list
    products.append(product)

    # Return the product as the response with status 201 Created
    return jsonify(product), 201

    

# GET /products: Retrieve all products
@app.route('/products', methods=['GET'])
def get_products():
    products = load_products()
    return jsonify(products), 200

if __name__ == '__main__':
    app.run(debug=True)