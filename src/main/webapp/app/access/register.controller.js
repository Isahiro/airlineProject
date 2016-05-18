'use strict';

(() => {
  angular
    .module('airline.access')
    .controller('RegisterController', RegisterController)

  RegisterController.$inject = ['accessService', '$state', '$log']

  function RegisterController (accessService, $state, $log) {
    this.errorMessage = undefined
    this.user

    $log.debug('RegisterController initializing')

    this.register = () => {
      $log.debug('Calling RegisterController.register()')
      $log.debug(this.user)

      accessService
        .register(this.user)()
        .then(result => {
          this.errorMessage = 'Username has been taken, please select a different one'
        })
    }
  }
})()
