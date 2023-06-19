import { CoreMenu } from "@core/types";
import { environment } from "environments/environment";

export const menu: CoreMenu[] =
  environment.uiRole == "domain"
    ? [
        {
          id: "domain_whitelist",
          title: "Domain_Whitelist",
          translate: "MENU.DOMAIN_WHITELIST",
          type: "item",
          icon: "search",
          url: "domain_whitelist",
        },
      ]
    : [
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
          id: "Gatway",
          type: "section",
          title: "Gatway",
          translate: "MENU.Gatway",
          icon: "home",
          children: [
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
           
          ],
        },
        {
          id: "Registry",
          type: "section",
          title: "Registry",
          translate: "MENU.Registry",
          icon: "home",
          children: [
            {
              id: "domain_whitelist",
              title: "Domain_Whitelist",
              translate: "MENU.DOMAIN_WHITELIST",
              type: "item",
              icon: "globe",
              url: "domain_whitelist",
            },
            {
              id: "Subscribe_Logs",
              title: "Subscribe_Logs",
              translate: "MENU.Subscribe_Logs",
              type: "item",
              icon: "activity",
              url: "subscribe_logs",
            },
            {
              id: "lookup_Changes",
              title: "lookup_Changes",
              translate: "MENU.lookup_Changes",
              type: "item",
              icon: "search",
              url: "lookup_Changes",
            },
            {
              id: "Server Performance",
              title: "Server Performance",
              translate: "MENU.server_performance",
              type: "item",
              icon: "activity",
              url: "server_performance",
            },
          ],
        },
        {
          id: "Analytics",
          type: "section",
          title: "Analytics",
          translate: "MENU.Analytics",
          icon: "home",
          children: [
            {
              id: "seller",
              title: "Seller Count",
              translate: "MENU.Seller",
              type: "item",
              icon: "activity",
              url: "seller",
            },
            {
              id: "buyer",
              title: "Buyer Count",
              translate: "MENU.Buyer",
              type: "item",
              icon: "search",
              url: "buyer",
            },
          ],
        },
      ];
