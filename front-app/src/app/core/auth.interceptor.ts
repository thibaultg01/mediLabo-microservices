import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const { basicAuthUser, basicAuthPassword } = environment;

  // Ne fait rien si les creds ne sont pas définis (pratique pour la prod)
  if (!basicAuthUser || !basicAuthPassword) {
    return next(req);
  }

  const token = btoa(`${basicAuthUser}:${basicAuthPassword}`);
  const authReq = req.clone({
    setHeaders: { Authorization: `Basic ${token}` },
  });
  return next(authReq);
};
