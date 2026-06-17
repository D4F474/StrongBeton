import { HttpInterceptorFn } from '@angular/common/http';

const API_URL = 'https://strongbeton-production.up.railway.app';
const LOCAL_API_URL = 'http://localhost:8081';

export const apiUrlInterceptor: HttpInterceptorFn = (req, next) => {
  const isExternalUrl =
    req.url.startsWith('http://') || req.url.startsWith('https://');

  if (isExternalUrl) {
    return next(req);
  }

  const apiReq = req.clone({
    url: `${LOCAL_API_URL}${req.url.startsWith('/') ? req.url : '/' + req.url}`,
  });

  return next(apiReq);
};