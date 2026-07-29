/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        background: '#0B0F19',
        surface: '#111827',
        'surface-hover': '#1F2937',
        border: 'rgba(255, 255, 255, 0.08)',
        accent: {
          cyan: '#06B6D4',
          indigo: '#6366F1',
          purple: '#A855F7',
          emerald: '#10B981',
          rose: '#F43F5E',
          amber: '#F59E0B'
        }
      },
      animation: {
        'pulse-glow': 'pulseGlow 2.5s cubic-bezier(0.4, 0, 0.6, 1) infinite',
        'float': 'float 4s ease-in-out infinite',
      },
      keyframes: {
        pulseGlow: {
          '0%, 100%': { opacity: 0.8, filter: 'drop-shadow(0 0 12px rgba(6, 182, 212, 0.6))' },
          '50%': { opacity: 0.4, filter: 'drop-shadow(0 0 4px rgba(6, 182, 212, 0.2))' },
        },
        float: {
          '0%, 100%': { transform: 'translateY(0px)' },
          '50%': { transform: 'translateY(-6px)' },
        }
      }
    },
  },
  plugins: [],
}
