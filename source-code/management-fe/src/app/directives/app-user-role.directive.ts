import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { ERole } from '@models/Role.enum';
import { AuthService } from '@services/auth.service';

@Directive({
  selector: '[hasRole]'
})
export class HasRoleDirective {

  @Input()
  set hasRole(roles: ERole[]) {
    if (!roles || !roles.length) {
      throw new Error('Roles value is empty or missed');
    }
    this.userRoles = roles;
  }
  userRoles: ERole[];

  constructor(
    private templateRef: TemplateRef<any>,
    private authService: AuthService,
    private viewContainer: ViewContainerRef
  ) { }

  ngOnInit(): void {
    let hasAccess = false;

    this.authService.authenticated().subscribe(res => {
      if (res && this.userRoles) {
        hasAccess = this.authService.hasRoles(this.userRoles);
      }

      if (hasAccess) {
        this.viewContainer.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainer.clear();
      }
    });
  }

}
