"use client";
import Link from "next/link";

export default function Sidebar({ className }: { className?: string }) {
  return (
    <aside className={`w-64 p-4 bg-gray-800 text-gray-100 h-full ${className}`}>
      <h2 className="font-bold text-lg mb-4">Menu</h2>
      <ul className="space-y-2">
        <li>
          <Link href="/commands" className="hover:text-primary">
            Commands
          </Link>
        </li>
        <li>
          <Link href="/keyboards" className="hover:text-primary">
            Keyboards
          </Link>
        </li>
        <li>
          <Link href="/integrations" className="hover:text-primary">
            Integrations
          </Link>
        </li>
      </ul>
    </aside>
  );
}
