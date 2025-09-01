# API Gateway Screenshots

This directory contains screenshots for the CloudShelf API Gateway setup guide.

## Screenshot Requirements

### 01-api-gateway-create-api.png

- AWS API Gateway console home page
- "Create API" button highlighted
- Shows different API types available (REST API, HTTP API, WebSocket API)

### 02-api-gateway-configure.png

- REST API configuration screen
- API name field showing "cloudshelf-api"
- Description field showing "CloudShelf Online Bookstore API"
- Endpoint type set to "Regional"
- "Create API" button visible

### 03-create-books-resource.png

- Create Resource dialog
- Resource Name: "books"
- Resource Path: "/books"
- "Enable API Gateway CORS" checkbox checked
- "Create Resource" button visible

### 04-create-books-get-method.png

- Create Method dropdown showing "GET" selected
- Integration setup screen for GET /books
- Integration type: "Lambda Function"
- Lambda Function field showing "cloudshelf-book-catalog"
- "Use Lambda Proxy integration" checkbox checked

### 05-books-all-methods.png

- Books resource expanded showing all methods (GET, POST, PUT, DELETE)
- Each method showing "Lambda Function" integration type
- Clear view of the resource structure

### 06-books-id-resource.png

- Books resource with {id} child resource created
- Shows path parameter configuration
- Methods under {id} resource (GET, PUT, DELETE)

### 07-create-cart-resource.png

- Create Resource dialog for cart
- Resource Name: "cart"
- Resource Path: "/cart"
- Create Resource screen

### 08-cart-userid-resource.png

- Cart resource with {userId} path parameter
- Shows the userId resource creation
- Methods configured under cart/{userId}

### 09-cart-items-resource.png

- Items sub-resource under cart/{userId}
- Shows nested resource structure
- Items resource with POST method

### 10-cors-configuration.png

- Enable CORS dialog
- Access-Control-Allow-Origin: "\*"
- Access-Control-Allow-Headers with appropriate headers listed
- Access-Control-Allow-Methods with methods selected

### 11-deploy-api.png

- Deploy API dialog
- Stage dropdown showing "dev" (new stage)
- Stage description field
- "Deploy" button visible

### 12-invoke-url.png

- Stage editor showing the deployed stage
- Invoke URL clearly visible and highlighted
- Stage configuration details

### 13-api-testing-results.png

- Terminal or API testing tool (Postman) showing successful API calls
- Example responses from both book catalog and shopping cart endpoints
- Clear demonstration that the API is working

### 14-complete-api-structure.png

- Full API Gateway console view showing complete resource tree
- All resources and methods visible
- Clean overview of the entire API structure
- Both /books and /cart hierarchies expanded

## Notes for Screenshot Capture

- Use consistent browser/console theme
- Ensure all text is readable
- Crop appropriately to focus on relevant UI elements
- Use consistent naming (cloudshelf-api, cloudshelf-book-catalog, cloudshelf-shopping-cart)
- Include any success messages or confirmations where relevant
