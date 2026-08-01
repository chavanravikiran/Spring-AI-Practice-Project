# AI Text Summarizer - Angular Frontend

A professional, modern Angular application for the AI Text Summarizer API built with Angular 19 and TypeScript.

## Features

### 🎨 Professional UI Design
- Modern gradient design with purple/blue theme
- Responsive layout for all screen sizes
- Smooth animations and transitions
- Custom scrollbars and focus states
- Clean, minimalist interface

### ✨ Main Summarizer Component
- Real-time character and word count
- Sample text quick-load buttons
- Large text input area
- Live compression statistics
- Copy-to-clipboard functionality
- Success/error notifications

### 📚 Summary History
- Grid view of all summaries
- Search functionality
- Sort by date or length
- Compression ratio badges
- Quick copy and delete actions
- Relative time display (e.g., "2 hours ago")

### 📄 Summary Detail View
- Full summary information
- Side-by-side comparison view
- Detailed statistics (compression ratio, characters saved, word counts)
- Download summary as text file
- Copy original or summarized text
- Delete functionality

## API Integration

The application integrates with all API endpoints:

- ✅ `GET /api/summarizer/health` - Health check
- ✅ `POST /api/summarizer/summarize` - Summarize text
- ✅ `GET /api/summarizer/summaries` - Get all summaries
- ✅ `GET /api/summarizer/summaries/:id` - Get summary by ID
- ✅ `DELETE /api/summarizer/summaries/:id` - Delete summary

## Technology Stack

- **Angular 19** - Latest Angular framework
- **TypeScript** - Type-safe development
- **SCSS** - Advanced styling with variables
- **Signals** - Modern reactive state management
- **HttpClient** - API communication
- **Router** - Client-side routing

## Project Structure

```
src/app/
├── components/
│   ├── summarizer/          # Main summarizer form
│   ├── summaries-list/      # Summary history list
│   └── summary-detail/      # Detailed summary view
├── services/
│   └── summarizer.service.ts  # API service
├── models/
│   └── summary.model.ts       # TypeScript interfaces
├── app-module.ts              # Main module
├── app-routing-module.ts      # Routing configuration
└── app.ts                     # Root component
```

## Getting Started

### Prerequisites

- Node.js 18+ and npm
- Angular CLI 19+
- Backend API running on `http://localhost:9207`

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend/ai-text-summarizer
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```
   or
   ```bash
   ng serve
   ```

4. Open your browser and navigate to:
   ```
   http://localhost:4200
   ```

### Build for Production

```bash
npm run build
```

The production build will be in the `dist/` directory.

## Configuration

### API Base URL

The API base URL is configured in `src/app/services/summarizer.service.ts`:

```typescript
private readonly API_BASE_URL = 'http://localhost:9207/api/summarizer';
```

To change the API URL, modify this constant.

## Routes

- `/` - Main summarizer page
- `/summaries` - Summary history list
- `/summary/:id` - Individual summary detail

## Key Components

### SummarizerService

Handles all API communication with reactive signals for state management:

```typescript
summaries = signal<Summary[]>([]);
loading = signal<boolean>(false);
error = signal<string | null>(null);
```

### Components

All components use Angular Signals for reactive state:

- **SummarizerComponent** - Main text input and summarization
- **SummariesListComponent** - List view with search and sort
- **SummaryDetailComponent** - Detailed view with statistics

## Styling

Global styles are in `src/styles.scss` with:
- Custom color scheme
- Utility classes
- Animations
- Responsive breakpoints
- Custom scrollbars

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Development Tips

1. **Hot Reload**: Changes to files automatically reload the browser
2. **Console Errors**: Check browser console for any errors
3. **Network Tab**: Monitor API calls in browser DevTools
4. **Angular DevTools**: Install Angular DevTools extension for debugging

## Troubleshooting

### API Connection Issues

If you get CORS errors:
- Ensure the backend API is running
- Check that the API URL is correct
- Verify CORS is enabled on the backend

### Build Errors

If you encounter build errors:
```bash
# Clear node_modules and reinstall
rm -rf node_modules
npm install

# Clear Angular cache
ng cache clean
```

### Port Already in Use

If port 4200 is already in use:
```bash
ng serve --port 4201
```

## Future Enhancements

- [ ] Dark mode toggle
- [ ] Export summaries as PDF
- [ ] Bulk operations (delete multiple)
- [ ] Summary categories/tags
- [ ] User authentication
- [ ] Summary sharing via link
- [ ] Offline mode with service workers

## Author

Created with Angular and professional UI/UX design principles.
