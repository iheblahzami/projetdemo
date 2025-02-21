# Use an official Node.js runtime as the base image
FROM node:18.20.5-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the Angular app for production
RUN npm run build -- --configuration production

# Install a lightweight HTTP server
RUN npm install -g http-server

# Expose the port the app runs on
EXPOSE 80

# Serve the built Angular app
CMD ["http-server", "dist/angular-15-crud", "-p", "80"]
