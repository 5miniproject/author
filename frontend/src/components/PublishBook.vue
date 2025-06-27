<template>

    <v-card outlined>
        <v-card-title>
            PublishBook
        </v-card-title>

        <v-card-text>
            <Number label="Id" v-model="value.id" :editMode="editMode"/>
            <Number label="AuthorId" v-model="value.authorId" :editMode="editMode"/>
            <String label="Contents" v-model="value.contents" :editMode="editMode"/>
            <String label="CoverImageUrl" v-model="value.coverImageUrl" :editMode="editMode"/>
            <String label="Plot" v-model="value.plot" :editMode="editMode"/>
            <String label="Status" v-model="value.status" :editMode="editMode"/>
            <String label="PlotUrl" v-model="value.plotUrl" :editMode="editMode"/>
            <String label="Category" v-model="value.category" :editMode="editMode"/>
            <Number label="SubscriptionFee" v-model="value.subscriptionFee" :editMode="editMode"/>
            <String label="Title" v-model="value.title" :editMode="editMode"/>
            <String label="Authorname" v-model="value.authorname" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="publishBook"
            >
                PublishBook
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
    name: 'PublishBookCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.id = 0;
        this.value.authorId = 0;
        this.value.contents = '';
        this.value.coverImageUrl = '';
        this.value.plot = '';
        this.value.status = '';
        this.value.plotUrl = '';
        this.value.category = '';
        this.value.subscriptionFee = 0;
        this.value.title = '';
        this.value.authorname = '';
    },
    watch: {
    },
    methods: {
        publishBook() {
            this.$emit('publishBook', this.value);
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

