# Deployment Guide

## Backend Deployment (Render/Heroku)

### Option 1: Deploy to Render

1. **Prepare the application**
   - Ensure you have a Render account at [render.com](https://render.com)
   - Fork this repository to your GitHub account

2. **Set up MySQL database**
   - Go to Render Dashboard → New → PostgreSQL
   - Name: `student-performance-db`
   - Region: Choose nearest region
   - Plan: Free tier
   - Click "Create Database"

3. **Deploy the backend**
   - Go to Render Dashboard → New → Web Service
   - Connect your GitHub repository
   - Build Command: `mvn clean package -DskipTests`
   - Start Command: `java -jar target/performance-analytics-0.0.1-SNAPSHOT.jar`
   - Environment Variables:
     ```
     SPRING_DATASOURCE_URL=jdbc:postgresql://<your-db-host>:5432/student_performance
     SPRING_DATASOURCE_USERNAME=<your-db-username>
     SPRING_DATASOURCE_PASSWORD=<your-db-password>
     JWT_SECRET=your-secret-key-change-this-in-production
     JWT_EXPIRATION=86400000
     ```
   - Click "Deploy Web Service"

### Option 2: Deploy to Heroku

1. **Install Heroku CLI**
   ```bash
   npm install -g heroku
   ```

2. **Login to Heroku**
   ```bash
   heroku login
   ```

3. **Create Heroku app**
   ```bash
   heroku create student-performance-api
   ```

4. **Add PostgreSQL database**
   ```bash
   heroku addons:create heroku-postgresql:mini
   ```

5. **Set environment variables**
   ```bash
   heroku config:set JWT_SECRET=your-secret-key-change-this-in-production
   heroku config:set JWT_EXPIRATION=86400000
   ```

6. **Deploy**
   ```bash
   git push heroku main
   ```

### Option 3: Deploy with Docker

1. **Build the Docker image**
   ```bash
   docker build -t student-performance-api .
   ```

2. **Run with Docker Compose**
   ```bash
   docker-compose up -d
   ```

3. **Push to Docker Hub (for cloud deployment)**
   ```bash
   docker tag student-performance-api your-dockerhub-username/student-performance-api
   docker push your-dockerhub-username/student-performance-api
   ```

## Frontend Deployment (Vercel/Netlify)

### Option 1: Deploy to Vercel

1. **Prepare the frontend**
   - Ensure you have a Vercel account at [vercel.com](https://vercel.com)
   - Fork the frontend repository to your GitHub account

2. **Deploy**
   - Go to Vercel Dashboard → Add New Project
   - Import your GitHub repository
   - Framework Preset: Create React App
   - Environment Variables:
     ```
     REACT_APP_API_URL=https://your-backend-url.com/api
     ```
   - Click "Deploy"

### Option 2: Deploy to Netlify

1. **Build the frontend**
   ```bash
   cd "Student Performance Analytics System2"
   npm run build
   ```

2. **Deploy**
   - Go to Netlify → Add new site → Deploy manually
   - Drag and drop the `build` folder
   - Or connect to GitHub for automatic deployments

3. **Set environment variables**
   - Site Settings → Environment Variables
   - Add: `REACT_APP_API_URL = https://your-backend-url.com/api`

## Post-Deployment Steps

1. **Update CORS configuration**
   - In `SecurityConfig.java`, add your deployed frontend URL:
   ```java
   configuration.setAllowedOrigins(Arrays.asList(
       "http://localhost:3000",
       "https://your-frontend-url.vercel.app"
   ));
   ```

2. **Test the deployed application**
   - Access the frontend URL
   - Login with credentials:
     - Teacher: `admin` / `admin123`
     - Student: `STU001` / `password`

3. **Monitor logs**
   - Render: Dashboard → Logs
   - Heroku: `heroku logs --tail`
   - Docker: `docker-compose logs -f`

## Troubleshooting

### Database Connection Issues
- Ensure database is running before deploying backend
- Check connection string in environment variables
- Verify database credentials

### CORS Errors
- Add your frontend URL to CORS configuration
- Restart the backend after updating CORS settings

### Build Failures
- Check Maven/Node.js versions
- Review build logs for specific errors
- Ensure all dependencies are in package.json/pom.xml

## Security Notes

- **Change default passwords** before production deployment
- **Use strong JWT secret** in production
- **Enable HTTPS** for both frontend and backend
- **Set up environment-specific configurations**
- **Regular security updates** for dependencies
