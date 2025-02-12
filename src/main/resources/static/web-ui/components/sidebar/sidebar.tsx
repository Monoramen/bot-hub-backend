"use client";
import React from "react";
import { sidebarConfig } from "@/config/sidebar";
import { FaHome, FaList, FaKeyboard, FaMousePointer, FaChartLine, FaCog } from "react-icons/fa";
import Link from "next/link";

const Sidebar = () => {
  const icons = {
    Home: <FaHome className="mr-2" />,
    Menubuilder: <FaList className="mr-2" />,
    Commands: <FaKeyboard className="mr-2" />,
    Keyboards: <FaKeyboard className="mr-2" />,
    Buttons: <FaKeyboard className="mr-2" />,
    InlineKeyboards: <FaKeyboard className="mr-2" />,
    InlineButtons: <FaKeyboard className="mr-2" />,
    Function: <FaMousePointer className="mr-2" />,
    Statistic: <FaChartLine className="mr-2" />,
    Settings: <FaCog className="mr-2" />,
  };

  const renderMenu = (items, level = 0) => {
    return items.map((item, index) => (
      <div key={index}>
        <div className={`flex items-center p-2 text-gray-300 ${level > 0 ? 'pl-6' : ''}`}>
          {icons[item.title]}
          {item.link ? (
            <Link href={item.link} className="hover:text-white">
              {item.title}
            </Link>
          ) : (
            <span>{item.title}</span>
          )}
        </div>
        {item.subMenu && (
          <div className={`${level > 0 ? 'pl-4' : 'pl-2'}`}>
            {renderMenu(item.subMenu, level + 1)}
          </div>
        )}
      </div>
    ));
  };

  return (
    <div className="fixed left-0 top-0 h-screen w-64 bg-gray-800 text-white">
      <div className="flex items-center justify-center h-16 bg-gray-900">
        <span className="text-xl font-semibold">Меню</span>
      </div>

      <nav className="flex-grow p-4 overflow-y-auto">
        {renderMenu(sidebarConfig.navItems)}
      </nav>
    </div>
  );
};

export default Sidebar;