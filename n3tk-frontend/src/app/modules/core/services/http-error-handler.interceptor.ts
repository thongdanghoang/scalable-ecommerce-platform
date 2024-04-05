import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Observable, catchError, map, throwError} from 'rxjs';

@Injectable()
export class HttpErrorHandlerInterceptor implements HttpInterceptor {
  constructor(
    // private readonly modalProvider
    // private readonly translateService if any
    private readonly route: Router
  ) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      map((event: HttpEvent<any>) => {
        return event;
      }),
      catchError((error: any) => {
        if (error.error && error.error.errorType === 'TECHNICAL') {
          // TODO: [thongdanghoang] show error message by modal
        }
        return throwError(() => error);
      })
    );
  }
}
