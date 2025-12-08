import { motion } from 'framer-motion';

export const Card = ({ children, className = '' }: { children: React.ReactNode; className?: string }) => (
  <motion.div 
    initial={{ opacity: 0, y: 10 }}
    animate={{ opacity: 1, y: 0 }}
    className={`bg-white dark:bg-slate-800 rounded-2xl shadow-xl shadow-slate-200/50 dark:shadow-black/30 border border-slate-100 dark:border-slate-700 overflow-hidden ${className}`}
  >
    {children}
  </motion.div>
);