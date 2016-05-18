'use strict';

(() => {
  angular
    .module('airline.access')
    .constant('accessRoutes', {
      register: {
        url: '/register',
        templateUrl: 'app/access/register.template.html',
        controller: 'RegisterController',
        controllerAs: 'registerCtrl',
        data: {
          loggedIn: false
        }
      }
    })
})()
