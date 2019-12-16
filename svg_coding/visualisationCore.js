window.addEventListener('load', (event) => {
    console.log('## SmartCity SVG Visualisation Core ##');
    var canvasWidth = 1000;
    var canvasHeight = 1000;
    var draw = SVG().addTo('body').size(canvasWidth, canvasHeight);
    //var rect = draw.rect(100, 100).attr({ fill: '#f06' });

    // Insert your testcode below
    var car_gas='<g id="g3735" <path        style="fill:#fefefe;stroke-width:0.31999999"        d="M 0,32 V 0 H 32 64 V 32 64 H 32 0 Z"        id="path3751" />     <path        style="fill:#f2f2f3;stroke-width:0.31999999"        d="M 0,62.135779 C 0,59.656029 0.74858042,58.88 3.1406244,58.88 H 4.8 L 4.80703,32.24 C 4.8136522,7.1293366 4.8476202,5.5265184 5.3987242,4.32 6.1692275,2.6331558 8.6123533,0.4128224 9.9712726,0.16442892 10.559073,0.05698642 16.956508,0.01203617 24.187797,0.06453946 L 37.335594,0.16 38.712608,1.1145739 c 0.757357,0.5250156 1.752848,1.5223014 2.212202,2.2161906 l 0.83519,1.2616169 0.08988,13.4992556 0.08988,13.499256 1.830124,0.199506 c 2.753184,0.300132 4.128826,0.780215 5.377722,1.876759 2.045549,1.796016 2.385603,2.903828 2.715536,8.846576 0.521901,9.400368 1.612611,12.266816 4.281536,11.252093 1.857619,-0.706265 2.591155,-4.495405 2.701869,-13.956685 l 0.07899,-6.750854 -2.3916,-2.140472 C 55.218554,29.740557 53.58831,27.973283 52.911168,26.990541 L 51.68,25.203738 51.569469,20.57719 51.458941,15.950642 47.809469,12.15545 C 44.753542,8.9774992 44.16,8.1984864 44.16,7.3655837 44.16,5.9937658 45.398346,4.8 46.821392,4.8 c 0.997926,0 1.671402,0.5919354 7.934547,6.973878 4.735984,4.825816 7.217443,7.610275 8.056282,9.04 L 64.024445,22.88 63.93041,36.8 c -0.09165,13.566291 -0.112589,13.971459 -0.824109,15.945187 -1.582733,4.390429 -3.532474,6.161891 -7.040205,6.396464 C 50.045773,59.54425 46.724128,54.56185 46.718202,45.12 46.715962,41.590262 46.268608,38.44855 45.686707,37.87633 45.463018,37.656358 44.524,37.377648 43.6,37.256966 L 41.92,37.037549 V 47.958774 58.88 h 1.731555 c 1.540701,0 1.822864,0.108518 2.56,0.984554 0.64576,0.767443 0.828445,1.331961 0.828445,2.56 V 64 H 23.52 0 Z"        id="path3749"/>     <path        style="fill:#99a3ad;stroke-width:0.31999999"        d="M 0,62.27545 C 0,59.854906 0.63695283,59.145981 2.9868387,58.95111 L 4.8,58.800749 4.803136,32.360374 4.806272,5.92 5.562673,4.32 C 5.9786934,3.44 6.7332867,2.3316389 7.2395469,1.8569753 9.1980346,0.02072039 9.3848822,-6.294848e-4 23.429742,0.00702784 35.208775,0.01344982 36.430365,0.0643112 37.60002,0.59701184 39.246036,1.3466643 41.143201,3.4553578 41.576941,5.0173507 41.79018,5.7852682 41.917114,11.014721 41.918234,19.077918 l 0.0019,12.837918 2.32,0.151495 c 2.392192,0.156208 4.383808,0.915866 5.427779,2.070311 0.301277,0.333158 0.867111,1.32048 1.257412,2.194051 0.601484,1.34624 0.745811,2.428262 0.946995,7.099641 0.260198,6.041568 0.797024,8.626967 2.051833,9.881776 0.93985,0.93985 1.730884,0.970452 2.825652,0.109309 1.545488,-1.21568 1.873379,-3.149641 2.048771,-12.083955 l 0.15904,-8.101536 -3.035047,-3.098463 C 51.735167,25.863722 51.692524,25.766622 51.622201,20.347292 L 51.564421,15.894584 48.04101,12.347292 C 46.053165,10.34595 44.428346,8.4315062 44.312771,7.9544653 c -0.320227,-1.3217587 0.587808,-2.7643015 1.89433,-3.0094064 0.59703,-0.1120035 1.357971,-0.057825 1.690979,0.1203958 0.993891,0.5319136 11.97591,11.8927833 13.665907,14.1373443 2.622515,3.483079 2.501466,2.578636 2.350935,17.565121 -0.115002,11.449117 -0.211786,13.578842 -0.691581,15.218266 -1.501095,5.129142 -3.635389,7.036316 -7.886711,7.047462 -2.118003,0.0055 -2.592105,-0.105312 -3.76246,-0.879818 C 48.12289,55.869882 46.980854,52.28735 46.603187,42.56 46.524605,40.536 46.355101,38.61697 46.226512,38.295488 45.93128,37.557382 44.797405,37.12 43.179168,37.12 H 41.92 v 10.840374 10.840375 l 1.779014,0.147529 c 2.337162,0.193815 2.997152,0.852762 3.243354,3.238208 L 47.129539,64 H 23.564769 0 Z M 36.059376,23.62422 C 36.795526,22.932641 36.8,22.87976 36.8,14.867374 36.8,7.1967549 36.767875,6.7654707 36.136774,5.9631549 L 35.473549,5.12 H 23.532225 c -11.277234,0 -11.983336,0.033994 -12.696774,0.6112672 -0.753157,0.6094109 -0.75574,0.6359427 -0.8504779,8.7377418 -0.092012,7.868696 -0.073512,8.153826 0.5832259,8.988733 L 11.246451,24.32 h 12.03615 c 11.91738,0 12.04346,-0.0069 12.776775,-0.69578 z"        id="path3747"/>     <path        style="fill:#7b8996;stroke-width:0.31999999"        d="M 0.01017708,62.32 C 0.01577447,61.396 0.20750976,60.393661 0.43625549,60.092579 0.97777843,59.379814 2.3234084,58.88 3.700832,58.88 H 4.8 V 32.850858 C 4.8,4.5796464 4.7732637,5.0340883 6.575575,2.6711364 7.0554442,2.0419954 8.1788835,1.1894611 9.0825504,0.76869066 L 10.72,0.00625211 23.36,0.01015389 C 35.394374,0.01386873 36.068995,0.04533514 37.44,0.6668895 39.231363,1.4790169 40.186026,2.3553215 41.071142,4 c 0.668874,1.242865 0.691559,1.6663779 0.781946,14.597939 l 0.09309,13.317939 2.228042,0.159415 c 5.763843,0.4124 7.648268,3.381885 7.668921,12.084707 0.0087,3.685971 0.608231,7.151072 1.49768,8.656787 1.069117,1.809869 3.237072,1.666714 4.229856,-0.279305 1.024103,-2.007408 1.32457,-4.755853 1.335312,-12.214416 l 0.01024,-7.105364 -3.074073,-3.167982 C 54.151402,28.30733 52.559254,26.381972 52.304035,25.771147 51.977171,24.98885 51.84,23.395841 51.84,20.382134 V 16.103712 L 48.153014,12.319727 C 46.125171,10.238536 44.381296,8.1981517 44.277738,7.7855402 43.852499,6.0912611 46.027613,4.4355584 47.744426,5.1466854 48.682858,5.5353968 56.785923,13.703237 60.416442,17.92 c 3.715427,4.315383 3.587321,3.693034 3.569456,17.341098 -0.0084,6.457606 -0.155853,12.649606 -0.327556,13.76 -1.085795,7.0219 -3.573833,10.015523 -8.298342,9.984611 -3.42104,-0.0224 -5.483178,-1.477482 -7.0056,-4.943325 -0.898317,-2.04505 -1.276688,-4.555277 -1.585085,-10.515962 -0.253677,-4.902976 -0.320144,-5.292713 -0.996016,-5.84 C 45.29185,37.31657 44.524662,37.12 43.48455,37.12 H 41.92 V 48 58.88 h 1.07225 c 1.477264,0 3.039161,0.591283 3.414505,1.292621 C 46.57904,60.494538 46.72,61.487392 46.72,62.378963 V 64 H 23.36 0 Z M 36.014544,23.854546 36.8,23.069091 V 14.734546 C 36.8,6.8266666 36.767235,6.3672368 36.16,5.76 35.54248,5.142479 35.093334,5.12 23.372466,5.12 c -12.084319,0 -12.15093,0.00364 -12.8,0.7003395 C 9.9513834,6.4869933 9.92,6.9112826 9.92,14.641377 c 0,8.45335 0.100942,9.160675 1.380919,9.676389 0.384494,0.154917 5.925627,0.290794 12.313627,0.30195 L 35.229091,24.64 Z"        id="path3745"/>     <path        style="fill:#657585;stroke-width:0.31999999"        d="M 0,62.295779 C 0,59.990621 0.80207334,59.2 3.1406244,59.2 H 4.8 V 33.010858 C 4.8,5.0191622 4.7938797,5.1355878 6.3826186,2.90441 6.7671805,2.3643424 7.7564122,1.5259135 8.580911,1.0412345 L 10.08,0.16 h 13.305094 13.305095 l 1.566445,0.9684594 c 0.970345,0.5999176 1.934988,1.5643241 2.534905,2.5342814 L 41.76,5.2285629 l 0.09607,13.3409451 0.09607,13.340946 2.128378,0.198749 c 1.288368,0.120311 2.79103,0.522957 3.807373,1.020208 2.854109,1.396384 3.593209,3.277834 3.830393,9.750589 0.329204,8.984019 1.455437,11.818102 4.402455,11.078448 2.000163,-0.502006 2.741027,-3.79009 2.984141,-13.24409 l 0.183238,-7.125644 -3.17584,-3.274357 C 54.365558,28.513461 52.689734,26.514237 52.388214,25.871639 51.955053,24.948476 51.84,23.793301 51.84,20.367279 V 16.03128 L 48.18137,12.23649 C 46.169123,10.149356 44.430598,8.0745827 44.317981,7.6258842 44.015869,6.4221722 45.31559,5.0133334 46.728186,5.0133334 c 0.970812,0 1.706124,0.6486903 7.970646,7.0316936 4.380762,4.463619 7.310208,7.722313 8.021168,8.922689 l 1.12,1.890995 -0.01712,13.130646 c -0.01856,14.246041 -0.152282,15.595977 -1.883219,19.011817 -1.021498,2.015831 -2.353242,3.108608 -4.575795,3.754724 -3.226868,0.938073 -6.585629,-0.37744 -8.26048,-3.235357 C 47.606288,52.965936 47.12279,50.38137 46.759574,42.991613 46.49609,37.630902 46.332336,37.318304 43.599987,36.960154 l -1.68,-0.220212 V 47.969971 59.2 h 1.704221 C 45.929379,59.2 46.72,60.002074 46.72,62.340624 V 64 H 23.36 0 Z M 35.956845,23.976774 36.8,23.313549 V 14.892225 C 36.8,7.0789571 36.75585,6.4163549 36.188563,5.7154509 L 35.57713,4.96 24.108565,4.8667731 C 17.800854,4.8154982 12.312337,4.8551603 11.91186,4.9549126 11.511384,5.054664 10.899384,5.4206163 10.55186,5.7681395 9.9550598,6.3649402 9.92,6.8624547 9.92,14.734546 v 8.334545 L 10.705454,23.854546 11.490909,24.64 H 23.3023 c 11.464724,0 11.836138,-0.01947 12.654545,-0.663226 z"        id="path3743"/>     <path        style="fill:#526475;stroke-width:0.31999999"        d="m 0,62.443155 c 0,-2.341344 0.79916582,-3.185584 3.1309205,-3.30751 L 4.96,59.04 5.12,32 C 5.2952678,2.3797098 5.1467235,4.415583 7.296785,2.1661371 9.3464781,0.02169931 9.8395302,-0.04012878 24.04488,0.06594769 L 36.64,0.16 38.139088,1.0412345 C 38.963587,1.5259135 39.952819,2.3643424 40.337382,2.90441 41.820419,4.9871434 41.92,6.0229811 41.92,19.366668 v 12.544951 l 2.101235,0.215277 c 2.838547,0.290819 4.423373,0.983098 5.720925,2.498995 1.361418,1.590509 1.683914,3.037616 1.92408,8.633744 0.216093,5.035181 0.621798,7.589587 1.469261,9.250746 0.643136,1.260653 1.139389,1.569619 2.521075,1.569619 1.069862,0 1.77536,-0.72319 2.381533,-2.441264 0.734758,-2.082528 0.861462,-3.40641 1.060201,-11.077683 l 0.188576,-7.278947 -3.097628,-3.037791 c -4.087975,-4.009006 -4.33322,-4.57316 -4.342231,-9.988675 L 51.84,16.03128 48.16,12.214326 C 45.068122,9.0073805 44.48,8.2377658 44.48,7.3986858 44.48,6.0767274 45.465949,5.12 46.828282,5.12 c 0.966556,0 1.691152,0.6322723 7.609558,6.64 6.980122,7.085465 9.072221,9.768265 9.41857,12.07788 0.117081,0.780759 0.143027,6.788637 0.05765,13.35084 -0.136198,10.469187 -0.231942,12.230394 -0.781318,14.372176 -1.228442,4.789178 -3.312727,7.003978 -6.830727,7.258448 C 53.383197,59.03047 52.275152,58.69336 50.533626,57.064365 47.994515,54.689322 47.195594,51.73071 46.766755,43.114672 46.492333,37.601059 46.333811,37.29177 43.6,36.936109 l -1.68,-0.21856 V 47.958774 59.2 h 1.704221 C 45.929379,59.2 46.72,60.002074 46.72,62.340624 V 64 H 23.36 0 Z M 36.062157,23.867707 36.96,23.095413 V 14.756596 C 36.96,6.5067546 36.9522,6.4100003 36.231466,5.688889 35.504227,4.9612995 35.482547,4.9598339 24.071464,4.8667731 17.784159,4.8154982 12.312337,4.8551603 11.91186,4.9549126 11.511384,5.054664 10.899384,5.4206163 10.55186,5.7681395 9.9545923,6.3654077 9.92,6.8629798 9.92,14.856774 v 8.456775 l 0.843155,0.663225 C 11.581468,24.620459 11.953355,24.64 23.38531,24.64 h 11.779 z"        id="path3741"/>     <path        style="fill:#3d5164;stroke-width:0.31999999"        d="m 0,62.443155 c 0,-2.341344 0.79916582,-3.185584 3.1309205,-3.30751 L 4.96,59.04 5.12,32 C 5.2992941,1.6993039 5.0996605,4.042913 7.690336,1.8253912 8.4092512,1.2100266 9.6287933,0.54757347 10.40043,0.35327322 11.297757,0.1273235 16.056719,0 23.604698,0 36.838627,0 37.338013,0.0681224 39.5068,2.169244 41.630042,4.2262406 41.6,3.9895094 41.6,18.663449 v 13.244004 l 2,0.197993 c 3.988493,0.394852 5.744576,1.358464 7.024976,3.854807 0.552586,1.077347 0.724058,2.201373 0.93359,6.119747 0.365338,6.83201 0.544519,8.108438 1.403956,10.00144 0.850089,1.87241 1.858102,2.507789 3.180742,2.004922 2.131293,-0.810314 2.727379,-3.441412 2.96153,-13.072064 L 59.29409,33.228595 56.255344,30.291808 C 52.088739,26.265004 52.03535,26.144406 51.896803,20.446443 L 51.783165,15.772886 48.131581,12.131663 C 44.964627,8.9736934 44.48,8.351721 44.48,7.4452198 44.48,6.070032 45.443818,5.12 46.838963,5.12 c 0.980083,0 1.652643,0.5851523 7.338896,6.385096 6.636736,6.769433 8.612851,9.147879 9.360083,11.265762 0.645149,1.828543 0.646439,21.031996 0.0016,25.869142 -0.965036,7.240627 -3.355395,10.24 -8.16079,10.24 -2.328202,0 -3.931402,-0.709174 -5.409869,-2.393059 C 47.991206,54.234493 47.1288,50.611139 46.920653,43.68 46.736992,37.56417 46.436454,37.045568 43.039862,36.983421 l -1.28,-0.02342 v 11.04 11.04 l 1.829079,0.09564 C 45.920835,59.257571 46.72,60.101811 46.72,62.443155 V 64 H 23.36 0 Z M 36.306698,23.637452 37.150285,22.634903 37.055142,14.489261 36.96,6.3436198 36.062157,5.5718099 35.16431,4.8 H 23.413358 C 10.593682,4.8 10.722642,4.7822496 9.9475818,6.6534115 9.6793024,7.3010957 9.5954486,9.8633155 9.6634803,15.334383 L 9.76,23.096454 10.657844,23.868227 11.555689,24.64 h 11.953709 11.953709 z"        id="path3739"/>     <path        style="fill:#33485d;stroke-width:0.31999999"        d="M 0.26977294,61.917728 C 0.4058465,59.73215 1.0248754,59.2 3.4312019,59.2 H 5.12 V 32.469722 C 5.12,3.2431488 5.0429677,4.488417 6.9769754,2.4506935 7.451639,1.9505749 8.3742634,1.2665749 9.0272518,0.9306935 10.138641,0.35902173 11.052321,0.32 23.32642,0.32 c 7.339238,0 13.457647,0.1314464 13.897199,0.298564 1.106295,0.4206121 3.029075,2.1738794 3.672727,3.3489381 C 41.376013,4.8431824 41.458832,6.5607274 41.6,18.56 l 0.16,13.6 2.4,0.185299 c 2.67319,0.206394 4.290848,0.792906 5.344726,1.937827 1.411911,1.533885 1.762615,3.043213 2.071888,8.916874 0.318327,6.045526 1.008224,9.336493 2.179444,10.396432 0.975664,0.882963 2.517974,0.76143 3.403625,-0.268202 1.487834,-1.729708 1.981085,-5.082614 2.027079,-13.779184 l 0.03451,-6.524841 -2.405916,-2.161833 C 55.492099,29.673365 53.868899,27.915703 53.20824,26.956455 L 52.00704,25.212369 51.893136,20.526185 51.779229,15.84 48.129613,12.130346 C 45.030698,8.9804474 44.48,8.2682387 44.48,7.4103456 44.48,6.1000157 45.462429,5.12 46.775987,5.12 c 0.879949,0 1.654269,0.6320093 5.782823,4.72 5.427369,5.374046 8.957443,9.346489 10.250406,11.534932 L 63.698432,22.88 63.593891,36.8 c -0.102016,13.58399 -0.122342,13.970128 -0.842054,15.99672 -1.504579,4.236646 -3.736515,6.069226 -7.391837,6.069226 -5.448365,0 -8.316282,-4.840157 -8.321853,-14.044717 -0.0022,-3.625562 -0.450653,-6.690877 -1.069401,-7.309626 -0.244567,-0.244566 -1.327588,-0.524729 -2.406708,-0.622579 L 41.6,36.71111 V 47.955555 59.2 h 1.774544 C 45.735885,59.2 46.72,60.089981 46.72,62.225456 V 63.68 l -23.28,3.2e-4 -23.28,3.2e-4 0.10977294,-1.763146 z M 36.291555,23.655448 37.12,22.670896 V 14.670833 6.6707693 L 36.184614,5.7353846 35.249232,4.8 H 23.455817 C 10.589743,4.8 10.722855,4.7817376 9.9475818,6.6534115 9.6794762,7.3006758 9.5954928,9.8510867 9.6634803,15.281018 c 0.095869,7.65675 0.1017485,7.714268 0.8722997,8.533844 L 11.311561,24.64 h 12.075773 12.075773 z"        id="path3737"/>   </g>'
    
    draw.svg(drone_icon);
    draw.svg(car_gas);


  });

var visualisationCore = {
    helloWorld: "Hi there",
    sayHelloWorld : function(){
        return "You asked me to say hello world. Hello world.";
    },
    sayHelloWorldByConsultant : function(){
        return _sayHelloWorldByConsultant();
    }


    


}

function _sayHelloWorldByConsultant(){
    return "ciao";
}
