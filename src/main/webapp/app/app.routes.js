'use strict';

(() => {
  angular
    .module('airline')
    .constant('airlineRoutes', {
      welcome: {
        url: '/',
        templateUrl: 'app/welcome.template.html',
        data: {
          loggedIn: false
        }
      }
    })
})()
