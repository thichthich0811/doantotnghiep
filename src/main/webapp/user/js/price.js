$(document).ready(function() {

    $("#min_price,#max_price").on('change', function() {

        $('#price-range-submit').show();

        var min_price_range = parseInt($("#min_price").val());

        var max_price_range = parseInt($("#max_price").val());

        if (min_price_range > max_price_range) {
            $('#max_price').val(min_price_range);
        }

        $("#slider-range").slider({
            values: [min_price_range, max_price_range]
        });

    });


    $("#min_price,#max_price").on("paste keyup", function() {

        $('#price-range-submit').show();

        var min_price_range = parseInt($("#min_price").val());

        var max_price_range = parseInt($("#max_price").val());

        if (min_price_range == max_price_range) {

            max_price_range = min_price_range + 100;

            $("#min_price").val(min_price_range);
            $("#max_price").val(max_price_range);
        }

        $("#slider-range").slider({
            values: [min_price_range, max_price_range]
        });

    });


    $(function() {
        var uls = new URL(document.URL)
        var smallPrice = uls.searchParams.get("smallPrice");
        var largePrice = uls.searchParams.get("largePrice");
        var a = 0;
        var b = 120000;
        if(smallPrice != null && largePrice !=null){
            a = smallPrice;
            b = largePrice;
        }
        $("#slider-range").slider({
            range: true,
            orientation: "horizontal",
            min: 0,
            max: 1000000,
            values: [a, b],
            step: 30000,

            slide: function(event, ui) {
                if (ui.values[0] == ui.values[1]) {
                    return false;
                }

                $("#min_price").val(ui.values[0]);
                $("#max_price").val(ui.values[1]);
            }
        });

        $("#min_price").val($("#slider-range").slider("values", 0));
        $("#max_price").val($("#slider-range").slider("values", 1));

    });


});


