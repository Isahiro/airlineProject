(() => {
  angular
    .module('airline')
    .controller('AppController', AppController)

  AppController.$inject = ['accessService', '$state']

  function AppController (accessService, $state) {
    this.accessService = accessService

    this.login = (credentials) => {
      this.loginError = undefined

      accessService
        .login(credentials)
        .then(response => {
          this.loginError = 'Invalid login'
        })
    }

    this.reviewTrips = () => {
      $state.go('reviewTrips')
    }

    this.planTrip = () => {
      $state.go('planTrip')
    }
  }
})()
