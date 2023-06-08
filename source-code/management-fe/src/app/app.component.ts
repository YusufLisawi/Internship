import { Component } from "@angular/core";
import { Title } from "@angular/platform-browser";
import {
  ActivatedRoute,
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router,
} from "@angular/router";
import { filter, map } from "rxjs/operators";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
})
export class AppComponent {
  title = "NTT DATA Train | Home";
  loadingRouteConfig: boolean;

  constructor(private router: Router, private titleService: Title) {}

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        this.loadingRouteConfig = true;
      } else if (
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel ||
        event instanceof NavigationError
      ) {
        this.loadingRouteConfig = false;
      }
    });
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map(() => {
          let route: ActivatedRoute = this.router.routerState.root;
          let routeTitle = "";
          route = route.firstChild;

          if (route.snapshot.data["title"]) {
            routeTitle = route.snapshot.data["title"];
          }
          if (route.firstChild) {
            route = route.firstChild;
            if (route.snapshot.data["title"]) {
              routeTitle += " | " + route!.snapshot.data["title"];
            }
          }
          return routeTitle;
        })
      )
      .subscribe((title: string) => {
        if (title) {
          this.titleService.setTitle(`${title}`);
        } else {
          this.titleService.setTitle(`NTT DATA Train`);
        }
      });
  }
}
