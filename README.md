# Trip Planner Website

A beautiful travel planning website with Kerala-inspired design featuring matte brown and matte green color scheme.

## 📁 File Structure

```
trip-planner/
├── index.html          # Main homepage
├── explore.html        # Detailed destination page
├── style.css           # Shared CSS styling
├── pexels-lina-12238221.jpg      # Dubai image
├── pexels-manjose-19872.jpg      # Northern China image
├── pexels-swastikarora-19743480.jpg  # Kerala image
└── README.md           # This file
```

## 🔗 File Connections

### **Main Navigation Flow:**
1. **index.html** (Homepage)
   - "Explore Now" button → `explore.html`
   - "View Details" buttons on destination cards → `explore.html#dubai`, `explore.html#china`, `explore.html#kerala`
   - Package modals → Auto-fill booking form
   - "Book Now" button → Booking section

2. **explore.html** (Destination Details)
   - "Choose Destination" buttons → `index.html#booking` (with selected destination)
   - "Book Now" button → `index.html#booking`
   - Navigation menu → Cross-page links

### **Styling:**
- Both HTML files link to `style.css` for consistent Kerala-inspired design
- Shared color variables and animations
- Responsive design for all screen sizes

### **Images:**
- All images are local files for fast loading
- Used in both pages for destination showcases

## 🎨 Design Features

### **Color Scheme (Kerala-Inspired):**
- **Matte Brown**: `#8B7355`
- **Matte Green**: `#6B8E23`
- **Light Brown**: `#D2B48C`
- **Dark Brown**: `#654321`
- **Cream**: `#F5F5DC`
- **Sage Green**: `#9CAF88`

### **Interactive Elements:**
- Hover effects on cards and buttons
- Modal popups for package details
- Smooth scrolling navigation
- Cross-page destination selection

## 🚀 How to Use

1. **Open `index.html`** in your browser
2. **Click "Explore Now"** to see detailed destination information
3. **Click "View Details"** on any destination card for specific information
4. **Click on package cards** to see detailed package information
5. **Select a destination and package** to proceed to booking
6. **Fill out the booking form** to complete your trip planning

## 📱 Responsive Design

The website is fully responsive and works on:
- Desktop computers
- Tablets
- Mobile phones

## 🛠️ Technologies Used

- **HTML5** - Structure and content
- **CSS3** - Styling and animations
- **Bootstrap 5** - Responsive framework
- **JavaScript** - Interactive functionality
- **Local Images** - Fast loading performance

## 🎯 Features

- ✅ Beautiful Kerala-inspired design
- ✅ Interactive package selection
- ✅ Detailed destination information
- ✅ Cross-page navigation
- ✅ Responsive layout
- ✅ Smooth animations
- ✅ Professional booking system
- ✅ Local image optimization

## Backend Setup

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL 15 or higher

### Database Setup
1. Install PostgreSQL if not already installed
2. Create a new database:
```sql
CREATE DATABASE tripyydb;
```

### Configuration
The backend is configured to use the following PostgreSQL settings (in `backend/src/main/resources/application.properties`):
- Database: `tripyydb`
- Username: `postgres`
- Password: `0111`
- Port: `5432`

Modify these settings if your PostgreSQL configuration is different.

### Building and Running
1. Navigate to the backend directory and build the project:
```bash
cd backend
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8082`.

### API Documentation
Swagger UI is available at `http://localhost:8082/swagger-ui.html` and provides interactive documentation for all endpoints.

### API Endpoints
- **Packages**
  - `GET /api/packages` - List all packages (supports pagination & sorting)
  - `GET /api/packages/{id}` - Get a package by ID
  - `POST /api/packages` - Create a new package (Requires Basic Auth)
  - `PUT /api/packages/{id}` - Update a package (Requires Basic Auth)
  - `DELETE /api/packages/{id}` - Delete a package (Requires Basic Auth)
  - `GET /api/packages/search?maxPrice=X` - Search packages by max price
- **Bookings**
  - `GET /api/bookings` - List all bookings (supports pagination & sorting)
  - `GET /api/bookings/{id}` - Get a booking by ID
  - `POST /api/bookings` - Create a new booking (Requires Basic Auth)
  - `PUT /api/bookings/{id}` - Update a booking (Requires Basic Auth)
  - `DELETE /api/bookings/{id}` - Delete a booking (Requires Basic Auth)
  - `GET /api/bookings/search?email=X` - Search bookings by email

### Running Tests
```bash
cd backend
mvn test
```

---

**Created with ❤️ for amazing travel experiences!** 