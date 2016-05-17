'use strict';

(() => {
  angular
    .module('airline.access')
    .service('accessService', AccessService)

  AccessService.$inject = []

  function AccessService () {
    this.currentUser = {
      "id": 1,
      "username": "Isahiro",
      "trips": [],
      "tickets": []
    }

    this.isLoggedIn = () => {
      return true
    }
  }
})()
