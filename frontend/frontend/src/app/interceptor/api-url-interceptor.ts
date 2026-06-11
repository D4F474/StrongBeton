import { HttpInterceptorFn } from '@angular/common/http';

const API_URL = 'https://strongbeton-production.up.railway.app';

export const apiUrlInterceptor: HttpInterceptorFn = (req, next) => {
  const isExternalUrl =
    req.url.startsWith('http://') || req.url.startsWith('https://');

  if (isExternalUrl) {
    return next(req);
  }

  const apiReq = req.clone({
    url: `${API_URL}${req.url.startsWith('/') ? req.url : '/' + req.url}`,
  });

  return next(apiReq);
};