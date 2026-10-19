import {CanActivateFn} from '@angular/router';
import {inject} from '@angular/core';
import {KeycloakService} from '../keycloak/keycloak.service';

export const authGuard: CanActivateFn = () => {
  const keycloak = inject(KeycloakService).keycloak;
  if (!keycloak.authenticated) {
    keycloak.login();
    return false;
  }
  return true;
};
