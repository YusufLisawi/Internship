import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

declare interface RouteInfo {
  pathBeca?: string;
  pathPfe?: string;
  title: string;
  icon: string;
  class: string;
}
export let ROUTES: RouteInfo[] = [
  {
    pathBeca: "/beca/dashboard",
    pathPfe: "/internship/dashboard",
    title: "Dashboard",
    icon: "fa-chart-pie",
    class: "",
  },
  {
    pathBeca: "/beca/session",
    pathPfe: "/internship/session",
    title: "SESSION",
    icon: " fa-dna",
    class: "",
  },
  {
    pathBeca: "/beca/candidate",
    pathPfe: "/internship/candidate",
    title: "Candidate",
    icon: "fa-copy",
    class: "",
  },
  {
    pathBeca: "/beca/calendar",
    title: "Calendar",
    icon: "fa-calendar",
    class: "",
  },
  {
    pathBeca: "/beca/interview",
    title: "INTERVIEW",
    icon: "fa-comments",
    class: "",
  },
  {
    pathPfe: "/internship/internship",
    title: "INTERNSHIP",
    icon: "fa-graduation-cap",
    class: "",
  },
  {
    pathBeca: "/beca/final-list",
    title: "FINAL LIST",
    icon: "fa-th-list",
    class: "",
  },
  {
    pathBeca: "/beca/settings",
    title: "SETTINGS",
    icon: "fa-sliders-h",
    class: "",
  },
];


@Component({
  selector: "app-sidebar",
  templateUrl: "./sidebar.component.html",
  styleUrls: ["./sidebar.component.css"],
})
export class SidebarComponent implements OnInit {
  menuItems: RouteInfo[];
  currentRoutePath = this.router.parent.routeConfig.path;

  constructor(private router: ActivatedRoute) {}

  ngOnInit() {
    
    this.menuItems = ROUTES.filter((menuItem) => menuItem);
  }

  isMobileMenu() {
    if (window.innerWidth > 991) {
      return false;
    }
    return true;
  }
}
