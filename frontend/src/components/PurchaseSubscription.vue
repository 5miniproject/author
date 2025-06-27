<template>

    <v-card outlined>
        <v-card-title>
            PurchaseSubscription
        </v-card-title>

        <v-card-text>
            <Boolean label="IsPurchased" v-model="value.isPurchased" :editMode="editMode"/>
            <Date label="PurchaseDate" v-model="value.purchaseDate" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="purchaseSubscription"
            >
                PurchaseSubscription
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>

export default {
    name: 'PurchaseSubscriptionCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.isPurchased = false;
        this.value.purchaseDate = '2025-06-27';
    },
    watch: {
    },
    methods: {
        purchaseSubscription() {
            this.$emit('purchaseSubscription', this.value);
        },
        close() {
            this.$emit('closeDialog');
        },
        change() {
            this.$emit("update:modelValue", this.value);
        },
    }
}
</script>

