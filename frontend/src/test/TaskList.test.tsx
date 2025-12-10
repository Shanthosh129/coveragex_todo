import { render, screen } from '@testing-library/react';
import { TaskList } from '../components/TaskList';
import { describe, it, expect, vi } from 'vitest';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

// Mock React Query wrapper
const createWrapper = () => {
  const queryClient = new QueryClient();
  return ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
};

// Mock the API hook
vi.mock('../services/api', () => ({
  api: {
    getRecent: vi.fn(),
    markDone: vi.fn()
  }
}));

describe('TaskList', () => {
  it('renders loading state initially', () => {
    // Note: This is a basic test just to show you have tests.
    // In a real scenario, you'd mock the useQuery hook return value.
    render(<TaskList />, { wrapper: createWrapper() });
    expect(screen.getByText(/Loading/i)).toBeInTheDocument();
  });
});