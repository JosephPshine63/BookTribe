import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, from, Observable, of, switchMap} from 'rxjs';
import {KeycloakService} from '../keycloak/keycloak.service';

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  constructor(
    private keycloakService: KeycloakService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const keycloak = this.keycloakService.keycloak;
    if (!keycloak.authenticated) {
      return next.handle(request);
    }
    // Refresh the access token if it expires within 30 seconds
    return from(keycloak.updateToken(30)).pipe(
      catchError(() => {
        keycloak.login();
        return of(false);
      }),
      switchMap(() => {
        const token = keycloak.token;
        const authReq = token
          ? request.clone({setHeaders: {Authorization: `Bearer ${token}`}})
          : request;
        return next.handle(authReq);
      })
    );
  }
}
