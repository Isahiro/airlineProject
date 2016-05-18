'use strict';

(() => {
  angular
    .module('airline',
    [
      'ngMaterial',
      'ngMessages',
      'ngAria',
      'ngAnimate',
      'ui.router',
      'dtrw.bcrypt',
      'airline.trip',
      'airline.access'
    ])
})()
