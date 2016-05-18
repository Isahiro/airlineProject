'use strict';

(() => {
  angular
    .module('airline.access')
    .config(config)

  config.$inject = ['accessRoutes', '$stateProvider']

  function config (accessRoutes, $stateProvider) {
    Object.keys(accessRoutes)
      .forEach(key => {
        $stateProvider
          .state(key, accessRoutes[key])
      })
  }
})()
