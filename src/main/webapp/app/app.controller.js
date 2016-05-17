(() => {
  angular
    .module('airline')
    .controller('AppController', AppController)

  AppController.$inject = ['accessService']

  function AppController (accessService) {
    this.accessService = accessService
  }
})()
