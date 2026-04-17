

//aggiungere colore territorio
let continenti=[{
    
    nome: "oceania",
    numeroArmateSupplementari: 2,
    territori: [
        {
            nome:"indonesia",
            numeroArmate:0,
            confinaCon: [
                "siam","nuova guinea","australia occidentale"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"nuova guindea",
            numeroArmate:0,
            confinaCon: [
                "australia orientale","indonesia","australia occidentale"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"australia occidentale",
            numeroArmate:0,
            confinaCon: [
                "australia orientale","indonesia","nuova guindea"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"australia orientale",
            numeroArmate:0,
            confinaCon: [
                "australia occidentale","nuova guindea"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        }
    ],
},

{
    nome: "sud america",
    numeroArmateSupplementari: 2,
    territori:[
        {
            nome:"venezuela",
            numeroArmate:0,
            confinaCon: [
                "america centrale","brasile","peru"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"brasile",
            numeroArmate:0,
            confinaCon: [
                "africa del nord","peru","venezuela" 
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"peru",
            numeroArmate:0,
            confinaCon: [
                "venezuela","brasile","argentina"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"argentina",
            numeroArmate:0,
            confinaCon: [
                "brasile","peru"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        }]
},
{
    nome:"europa",
    numeroArmateSupplementari:3,
    territori:[
        {
            nome:"islanda",
            numeroArmate:0,
            confinaCon: [
                "groenlandia","scandinavia","gran bretagna"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"scandinavia",
            numeroArmate:0,
            confinaCon: [
                "islanda","gran bretagna","europa settentrionale","ucraina"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"gran bretagna",
            numeroArmate:0,
            confinaCon: [
                "islanda","scandinavia","europa settentrionale","europa occidentale"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"europa settentrionale",
            numeroArmate:0,
            confinaCon: [
                "scandinavia","gran bretagna","europa occidentale","ucraina","europa meridionale"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"europa occidentale",
            numeroArmate:0,
            confinaCon: [
                "africa del nord","gran bretagna","europa settentrionale","europa meridionale"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"europa meridionale",
            numeroArmate:0,
            confinaCon: [
                "europa occidentale","africa del nord","europa settentrionale","ucraina" ,"egitto","medio oriente"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"ucraina",
            numeroArmate:0,
            confinaCon: [
                "europa settentrionale","urali","europa meridionale","scandinavia" ,"medio oriente","afghanistan"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        }
    ]
},
{
    nome:"africa",
    numeroArmateSupplementari:3,
    territori:[
        {

            nome:"africa del nord",
            numeroArmate:0,
            confinaCon: [
                "europa occidentale","egitto","congo","europa meridionale" ,"egitto","africa orientale"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"egitto",
            numeroArmate:0,
            confinaCon: [
                "europa meridionale","africa del nord","africa orientale","medio oriente","congo"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"africa orientale",
            numeroArmate:0,
            confinaCon: [
                "congo","africa del nord","madagascar","egitto","africa del sud"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"congo",
            numeroArmate:0,
            confinaCon: [
                "africa del nord","africa orientale","africa del sud"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"madagascar",
            numeroArmate:0,
            confinaCon: [
                "africa orientale","africa del sud"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"africa del sud",
            numeroArmate:0,
            confinaCon: [
                "congo","africa orientale","madagascar"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        }
    ]
},
{
    nome:"nord america",
    numeroArmateSupplementari:3,
    territori:[{
            nome:"groenlandia",
            numeroArmate:0,
            confinaCon: [
                "islanda","quebec","ontario","territori del nord ovest"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"quebec",
            numeroArmate:0,
            confinaCon: [
                "groenlandia","ontario","stati uniti orientali"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"ontario",
            numeroArmate:0,
            confinaCon: [
                "quebec","alberta","territori del nord ovest","groenlandia"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"stati uniti occidentali",
            numeroArmate:0,
            confinaCon: [
                "ontario","alberta","stati uniti orientali","america centrale"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"america centrale",
            numeroArmate:0,
            confinaCon: [
                "venezuela","stati uniti occidentali","stati uniti orientali"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"alberta",
            numeroArmate:0,
            confinaCon: [
                "ontario","territori del nord ovest","stati uniti orientali","alaska"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"territori del nord ovest",
            numeroArmate:0,
            confinaCon: [
                "ontario","groenlandia","alberta","alaska"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"alaska",
            numeroArmate:0,
            confinaCon: [
                "kamchatka","territori del nord ovest","alberta"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"stati uniti orientali",
            numeroArmate:0,
            confinaCon: [
                "america centrale","ontario","stati uniti occidentali","quebec"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        }]
},
{
    nome:"asia",
    numeroArmateSupplementari:3,
        territori:[{
            nome:"kamchatka",
            numeroArmate:0,
            confinaCon: [
                "cita","giappone","jacuzia","alaska" ,"mongolia"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"jacuzia",
            numeroArmate:0,
            confinaCon: [
                "cita","siberia","kamchatka"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"siberia",
            numeroArmate:0,
            confinaCon: [
                "cita","urali","jacuzia","cina","mongolia"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"urali",
            numeroArmate:0,
            confinaCon: [
                "afghanistan","ucraina","siberia","cina"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"afghanistan",
            numeroArmate:0,
            confinaCon: [
                "ucraina","urali","medio oriente","cina"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"medio oriente",
            numeroArmate:0,
            confinaCon: [
                "europa meridionale","ucraina","jacuzia","alaska" ,"egitto","mongolia"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"india",
            numeroArmate:0,
            confinaCon: [
                "medio oriente","cina","siam"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"siam",
            numeroArmate:0,
            confinaCon: [
                "india","cina","indonesia"
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"giappone",
            numeroArmate:0,
            confinaCon: [
                "mongolia","kamchatka" 
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"cita",
            numeroArmate:0,
            confinaCon: [
                "siberia","kamchatka","jacuzia","mongolia"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        },{
            nome:"mongolia",
            numeroArmate:0,
            confinaCon: [
                "siberia","giappone","kamchatka","cina","mongolia"  
            ],
            propretario:"nickNameX",
            colore:"#000000"
        }]
}]


























var territori=[{
    nome:"cina",
    numeroArmate:0,
    confinaCon: [
        "mongolia","siberia","urali","afghanistan","medio oriente","india","siam"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
    
},{
    nome:"groenlandia",
    numeroArmate:0,
    confinaCon: [
        "islanda","quebec","ontario","territori del nord ovest"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"quebec",
    numeroArmate:0,
    confinaCon: [
        "groenlandia","ontario","stati uniti orientali"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"ontario",
    numeroArmate:0,
    confinaCon: [
        "quebec","alberta","territori del nord ovest","groenlandia"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"stati uniti occidentali",
    numeroArmate:0,
    confinaCon: [
        "ontario","alberta","stati uniti orientali","america centrale"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"america centrale",
    numeroArmate:0,
    confinaCon: [
        "venezuela","stati uniti occidentali","stati uniti orientali"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"alberta",
    numeroArmate:0,
    confinaCon: [
        "ontario","territori del nord ovest","stati uniti orientali","alaska"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"territori del nord ovest",
    numeroArmate:0,
    confinaCon: [
        "ontario","groenlandia","alberta","alaska"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"alaska",
    numeroArmate:0,
    confinaCon: [
        "kamchatka","territori del nord ovest","alberta"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"stati uniti orientali",
    numeroArmate:0,
    confinaCon: [
        "america centrale","ontario","stati uniti occidentali","quebec"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"venezuela",
    numeroArmate:0,
    confinaCon: [
        "america centrale","brasile","peru"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"brasile",
    numeroArmate:0,
    confinaCon: [
        "africa del nord","peru","venezuela" 
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"peru",
    numeroArmate:0,
    confinaCon: [
        "venezuela","brasile","argentina"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"argentina",
    numeroArmate:0,
    confinaCon: [
        "brasile","peru"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"islanda",
    numeroArmate:0,
    confinaCon: [
        "groenlandia","scandinavia","gran bretagna"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"scandinavia",
    numeroArmate:0,
    confinaCon: [
        "islanda","gran bretagna","europa settentrionale","ucraina"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"gran bretagna",
    numeroArmate:0,
    confinaCon: [
        "islanda","scandinavia","europa settentrionale","europa occidentale"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"europa settentrionale",
    numeroArmate:0,
    confinaCon: [
        "scandinavia","gran bretagna","europa occidentale","ucraina","europa maridionale"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"europa occidentale",
    numeroArmate:0,
    confinaCon: [
        "africa del nord","gran bretagna","europa settentrionale","europa meridionale"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"europa meridionale",
    numeroArmate:0,
    confinaCon: [
        "europa occidentale","africa del nord","europa settentrionale","ucraina" ,"egitto","medio oriente"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"ucraina",
    numeroArmate:0,
    confinaCon: [
        "europa settentrionale","urali","europa meridionale","scandinavia" ,"medio oriente","afghanistan"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"africa del nord",
    numeroArmate:0,
    confinaCon: [
        "europa occidentale","egitto","congo","europa meridionale" ,"egitto","africa orientale"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"egitto",
    numeroArmate:0,
    confinaCon: [
        "europa meridionale","africa del nord","africa orientale","medio oriente","congo"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"africa orientale",
    numeroArmate:0,
    confinaCon: [
        "congo","africa del nord","madagascar","egitto","africa del sud"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"congo",
    numeroArmate:0,
    confinaCon: [
        "africa del nord","africa orientale","africa del sud"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"madagascar",
    numeroArmate:0,
    confinaCon: [
        "africa orientale","africa del sud"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"africa del sud",
    numeroArmate:0,
    confinaCon: [
        "congo","africa orientale","madagascar"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"kamchatka",
    numeroArmate:0,
    confinaCon: [
        "cita","giappone","jacuzia","alaska" ,"mongolia"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"jacuzia",
    numeroArmate:0,
    confinaCon: [
        "cita","siberia","kamchatka"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"siberia",
    numeroArmate:0,
    confinaCon: [
        "cita","urali","jacuzia","cina","mongolia"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"urali",
    numeroArmate:0,
    confinaCon: [
        "afghanistan","ucraina","siberia","cina"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"afghanistan",
    numeroArmate:0,
    confinaCon: [
        "ucraina","urali","medio oriente","cina"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"medio oriente",
    numeroArmate:0,
    confinaCon: [
        "europa meridionale","ucraina","jacuzia","alaska" ,"egitto","mongolia"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"india",
    numeroArmate:0,
    confinaCon: [
        "medio oriente","cina","siam"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"siam",
    numeroArmate:0,
    confinaCon: [
        "india","cina","indonesia"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"giappone",
    numeroArmate:0,
    confinaCon: [
        "mongolia","kamchatka" 
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"cita",
    numeroArmate:0,
    confinaCon: [
        "siberia","kamchatka","jacuzia","mongolia"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"mongolia",
    numeroArmate:0,
    confinaCon: [
        "siberia","giappone","kamchatka","cina","mongolia"  
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"indonesia",
    numeroArmate:0,
    confinaCon: [
        "siam","nuova guinea","australia occidentale"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"nuova guinea",
    numeroArmate:0,
    confinaCon: [
        "australia orientale","indonesia","australia occidentale"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"australia occidentale",
    numeroArmate:0,
    confinaCon: [
        "australia orientale","indonesia","nuova guindea"
    ],
    propretario:"nickNameX",
    colore:"#000000"
},{
    nome:"australia orientale",
    numeroArmate:0,
    confinaCon: [
        "australia occidentale","nuova guindea"
    ],
    propretario:"nickNameX",
    colore:"#000000"
}
]

