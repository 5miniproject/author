<template>

    <v-card outlined>
        <v-card-title>
            ScriptPublishRequest
        </v-card-title>

        <v-card-text>
            <Number label="Id" v-model="value.id" :editMode="editMode"/>
            <Number label="AuthorId" v-model="value.authorId" :editMode="editMode"/>
            <String label="Contents" v-model="value.contents" :editMode="editMode"/>
            <String label="Status" v-model="value.status" :editMode="editMode"/>
            <String label="Authorname" v-model="value.authorname" :editMode="editMode"/>
            <String label="Title" v-model="value.title" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="scriptPublishRequest"
            >
                ScriptPublishRequest
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
    name: 'ScriptPublishRequestCommand',
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
        this.value.status = '';
        this.value.authorname = '';
        this.value.title = '';
    },
    watch: {
    },
    methods: {
        scriptPublishRequest() {
            this.$emit('scriptPublishRequest', this.value);
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

