"use client";
import { Navbar, NavbarBrand, NavbarContent, NavbarItem, Link, Button } from "@nextui-org/react";

export const NewLogo = () => (
  <svg fill="none" height="36" viewBox="0 0 32 32" width="36" className="mr-2">
    <path
      clipRule="evenodd"
      d="M16 3C9.373 3 4 8.373 4 15C4 21.627 9.373 27 16 27C22.627 27 28 21.627 28 15C28 8.373 22.627 3 16 3ZM16 25C10.477 25 6 20.523 6 15C6 9.477 10.477 5 16 5C21.523 5 26 9.477 26 15C26 20.523 21.523 25 16 25Z"
      fill="currentColor"
      fillRule="evenodd"
    />
    <text x="50%" y="50%" textAnchor="middle" fill="white" fontSize="16" fontWeight="bold" dy="6">
      B
    </text>
  </svg>
);

export default function App() {
  return (
    <div className="flex flex-col justify-center w-full bg-blue-900">
      <Navbar isBordered className="sticky top-0 z-15 mx-auto w-full max-w-screen-xl border-b-4 border-blue-500 overflow-hidden">
        <NavbarBrand className="flex items-center">
          <NewLogo />
          <Link href="/" className="font-bold text-white ml-2">
            BotHub
          </Link>
        </NavbarBrand>

        {/* Центрирование меню */}
        <NavbarContent justify="center" className="flex gap-4">
          <NavbarItem>
            <Link color="foreground" href="/commands">
              Commands
            </Link>
          </NavbarItem>
          <NavbarItem>
            <Link color="foreground" href="/keyboards">
              Keyboards
            </Link>
          </NavbarItem>
          <NavbarItem>
            <Link color="foreground" href="/integrations">
              Integrations
            </Link>
          </NavbarItem>
        </NavbarContent>

        {/* Центрирование кнопок */}
        <NavbarContent justify="center" className="flex gap-4">
          <NavbarItem>
            <Link href="/login" className="text-white">
              Login
            </Link>
          </NavbarItem>
          <NavbarItem>
            <Button as={Link} color="primary" href="/signup" variant="flat">
              Sign Up
            </Button>
          </NavbarItem>
        </NavbarContent>
      </Navbar>
    </div>
  );
}
