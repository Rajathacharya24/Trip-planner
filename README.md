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
- Docker (for running integration tests)

### Database Setup
1. Install PostgreSQL if not already installed
2. Create a new database:
```sql
CREATE DATABASE trip_planner_db;
```

### Configuration
The backend is configured to use the following PostgreSQL settings (in `src/main/resources/application.properties`):
- Database: `trip_planner_db`
- Username: `postgres`
- Password: `postgres`
- Port: `5432`

Modify these settings if your PostgreSQL configuration is different.

### Building and Running
1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`.

### API Endpoints
- `POST /api/bookings` - Create a new booking
- `GET /api/bookings/{id}` - Get a booking by ID

### Running Tests
```bash
mvn test
```

Integration tests use Testcontainers and require Docker to be running.

---

**Created with ❤️ for amazing travel experiences!** 