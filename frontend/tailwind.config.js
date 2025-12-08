/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class', // <--- IMPORTANT: Enables manual dark mode toggling
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // Custom palette that looks good in both modes
        primary: {
          light: '#4f46e5', // Indigo 600
          dark: '#818cf8',  // Indigo 400
        },
        surface: {
          light: '#ffffff',
          dark: '#1e293b',  // Slate 800
        }
      }
    },
  },
  plugins: [],
}