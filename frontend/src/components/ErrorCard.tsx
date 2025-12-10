

export const ErrorCard = ({ title = 'Error', messages = [] as string[] }: { title?: string; messages?: string[] }) => {
  if (!messages || messages.length === 0) return null;
  return (
    <div className="mb-4 p-4 rounded-lg bg-red-50 dark:bg-red-900/30 border border-red-200 dark:border-red-700 text-red-700 dark:text-red-200">
      <div className="font-semibold">{title}</div>
      <ul className="mt-2 list-disc list-inside text-sm">
        {messages.map((m, i) => (
          <li key={i}>{m}</li>
        ))}
      </ul>
    </div>
  );
};

export default ErrorCard;
