import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
     <div>
        <h1>{{pageTitle}}</h1>
     </div>
     `
})
export class AppComponent {
  pageTitle: string = 'Andys new Angular project ';
}

@Component({
  selector: 'ajg-cake',
  template: `
     <div>
     <h1>{{cakeType}}</h1>
     <h3>{{cakeUsp}}</h3>
     </div>
     `
})
export class AppComponent2 {
  cakeType: string = 'Black forest gateaux';
  cakeUsp: string = 'Its got cherries in it ...';
}
