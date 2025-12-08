import { Moon, Sun } from 'lucide-react';
import { useTheme } from '../context/ThemeContext';
import { motion } from 'framer-motion';

export const ThemeToggle = () => {
  const { theme, toggleTheme } = useTheme();

  return (
    <button
      onClick={toggleTheme}
      className="p-2 rounded-full bg-slate-100 dark:bg-slate-700 text-slate-800 dark:text-yellow-400 transition-colors relative overflow-hidden"
    >
      <motion.div
        initial={false}
        animate={{ rotate: theme === 'dark' ? 180 : 0 }}
        transition={{ duration: 0.5 }}
      >
        {theme === 'dark' ? <Moon size={20} /> : <Sun size={20} />}
      </motion.div>
    </button>
  );
};