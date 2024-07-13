FROM nginx:alpine

# Copy custom configuration file from the current directory
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Remove default nginx static assets
RUN rm -rf /usr/share/nginx/html/*

# Copy static assets from builder stage
COPY dist /usr/share/nginx/html

# Containers run nginx with global directives and daemon off
ENTRYPOINT ["nginx", "-g", "daemon off;"]