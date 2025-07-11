import { SidebarDemo } from "../components/ui/sidebarComponent";

interface SubPageProps {
  children: React.ReactNode;
}

export default function UserPage({ children }: SubPageProps) {
  return (
    <>
      <SidebarDemo>{children}</SidebarDemo>
    </>
  );
}
