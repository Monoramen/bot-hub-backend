

export const sidebarConfig = {
  navItems: [
  {
    title: "Home",
    link: "#",
  },
  {
    title: "Menubuilder",
    subMenu: [
      {
        title: "Commands",
        link: "/dashboard/commands",
      },
      {
        title: "InlineKeyboards",
        link: "/dashboard/inlineKeyboards",
        subMenu: [
          {
            title: "InlineButtons",
            link: "/dashboard/inlineButtons",
          },
        ],
      },
      {
        title: "Keyboards",
        link: "/dashboard/keyboards",
        subMenu: [
          {
            title: "Buttons",
            link: "/dashboard/buttons",
          },
        ],
      },
    ],
  },
  {
    title: "Function",

    link: "/dashboard/function",
  },
  {
    title: "Statistic",
    link: "/dashboard/statistic",
  },
  {
    title: "Settings",
    link: "/dashboard/settings",
  },
],

};