<template>
    <div>
        <Number
            label="SubscriberId"
            v-model="value.subscriberId"
            :editMode="editMode"
        />
        <Number
            label="AuthorId"
            v-model="value.authorId"
            :editMode="editMode"
        />
        <Number
            label="BookId"
            v-model="value.bookId"
            :editMode="editMode"
        />
        <Boolean
            label="IsSubscribed"
            v-model="value.isSubscribed"
            :editMode="editMode"
        />
        <String
            label="Status"
            v-model="value.status"
            :editMode="editMode"
        />
        <Date
            label="SubscriptionDate"
            v-model="value.subscriptionDate"
            :editMode="editMode"
        />
        <Date
            label="SubscriptionExpiredDate"
            v-model="value.subscriptionExpiredDate"
            :editMode="editMode"
        />
        <String
            label="Title"
            v-model="value.title"
            :editMode="editMode"
        />
        <v-row class="ma-0 pa-0">
            <v-spacer></v-spacer>
            <v-btn width="64px" color="primary" @click="save">
                저장
            </v-btn>
        </v-row>
    </div>
</template>


<script>
import BaseEntity from './base-ui/BaseEntity.vue'

export default {
    name: 'SubscribeBook',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "subscribeBooks",
        value: {
        }
    }),
    created(){
    },
    computed:{
    },
    methods: {
    },
}
</script>
