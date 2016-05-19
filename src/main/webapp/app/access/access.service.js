'use strict';

(() => {
  angular
    .module('airline.access')
    .service('accessService', AccessService)

  AccessService.$inject = ['bcrypt', '$http', '$state']

  function AccessService (bcrypt, $http, $state) {
    this.currentUser = {
      'id': 1,
      'username': 'Isahiro'
    }

    this.register = (user) => {
      let salt = bcrypt.genSaltSync(4)
      let hash = bcrypt.hashSync(user.password, salt)
      user.password = hash

      return $http
        .post('./api/users', user)
        .then(response => response.data)
        .then(user => {
          if (user.id == null) {
            //  user already exists
            return null
          } else {
            this.currentUser = user
            $state.go('reviewTrips')
          }
        })
    }

    this.login = (credentials) => {
      return $http
        .get('./api/users/login/' + credentials.username)
        .then(response => response.data)
        .then(user => {
          if (user.id == null) {
            return null
          } else {
            if (bcrypt.compareSync(credentials.password, user.password)) {
              this.currentUser = user
              delete this.currentUser.password
              credentials = undefined
              $state.go('reviewTrips')
            } else {
              return null
            }
          }
        })
    }

    this.logout = () => {
      this.currentUser = undefined
      $state.go('welcome')
    }

    this.isLoggedIn = () => {
      return this.currentUser !== undefined
    }
  }
})()
