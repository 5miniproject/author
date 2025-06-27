<template>
    <div>
        <String
            label="Email"
            v-model="value.email"
            :editMode="editMode"
        />
        <String
            label="Name"
            v-model="value.name"
            :editMode="editMode"
        />
        <Boolean
            label="IsPurchased"
            v-model="value.isPurchased"
            :editMode="editMode"
        />
        <Date
            label="RegisterDate"
            v-model="value.registerDate"
            :editMode="editMode"
        />
        <Date
            label="PurchaseDate"
            v-model="value.purchaseDate"
            :editMode="editMode"
        />
        <String
            label="Notification"
            v-model="value.notification"
            :editMode="editMode"
        />
        <Boolean
            label="IsKt"
            v-model="value.isKt"
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
    name: 'Subscriber',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "subscribers",
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
