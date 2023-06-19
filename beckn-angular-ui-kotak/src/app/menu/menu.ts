import { CoreMenu } from "@core/types";

export const menu: CoreMenu[] = [
  {
    id: "Home",
    type: "section",
    title: "Dashboard",
    translate: "MENU.HOME",
    icon: "home",
    children: [
      {
        id: "transaction",
        title: "Transaction",
        translate: "MENU.TRANSACTION",
        type: "item",
        icon: "activity",
        url: "home",
      },
      {
        id: "lookup_transaction",
        title: "Lookup Transaction",
        translate: "MENU.LOOKUP",
        type: "item",
        icon: "search",
        url: "lookup_transaction",
      },
      
      {
        id: "seller_transaction",
        title: "Seller transaction",
        translate: "MENU.SELLER_TRANSACTION",
        type: "item",
        icon: "arrow-up-right",
        url: "seller_transaction",
      },
      {
        id: "buyer_transaction",
        title: "Buyer Transaction",
        translate: "MENU.BUYER_TRANSACTION",
        icon: "bold",
        type: "item",
        url: "buyer_transaction",
      },
    ],
  },
  {
    id: "transactions",
    title: "Transactions",
    translate: "MENU.TRANSACTION",
    type: "item",
    icon: "trending-up",
    url: "transactions",
  },
  {
    id: "summary_report",
    title: "Summary_report",
    translate: "MENU.SUMMARY_REPORT",
    type: "item",
    icon: "file",
    url: "summary_report",
  },
];
