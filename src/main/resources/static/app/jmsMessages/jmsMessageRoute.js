angular.module('jmsMessageApp').
    config(['$stateProvider',
        function config($stateProvider) {
        
        var jmsListState = {
            name:'jmsList',
            url:'/jmsList',
            params: {
                data: null
            },
            template: '<message-list></message-list>'

        }

        var jmsQueueList = {
            name:'queueList',
            url:'/queues',
            template: '<queue-list></queue-list>'

        }

        var jmsMessageDetail = {
                name:'detail',
                url:'/message-det',
                template: '<message-detail></message-detail>',
                params: {
                    data: null
                },
            }
        
        $stateProvider.state(jmsListState);
        $stateProvider.state(jmsQueueList);
        $stateProvider.state(jmsMessageDetail);

    }
]);
