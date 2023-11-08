package com.barrybarry.coffeeport.constant;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Config {
    public static String veraVersion = "3,8,6,7";
    public static int httpPort = 16106;
    public static int httpsPort = 16105;

    // セキュリティのために変更してはいけない!
    public static String host = "127.0.0.1";

    // 変更時、CMS検証に失敗
    public static String distSignRootCA = """
-----BEGIN CERTIFICATE-----
MIIDNjCCAh6gAwIBAgIUKRy+35t884Ssb96zfJChRDX5ZvgwDQYJKoZIhvcNAQEL
BQAwOjELMAkGA1UEBhMCS1IxEDAOBgNVBAoTB1dJWlZFUkExGTAXBgNVBAMTEFZF
UkFQT1JUIFJvb3QgQ0EwIBcNMjAwNzIxMDczMTI1WhgPMjA1MDA3MTQwNzMxMjVa
MDoxCzAJBgNVBAYTAktSMRAwDgYDVQQKEwdXSVpWRVJBMRkwFwYDVQQDExBWRVJB
UE9SVCBSb290IENBMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1IDD
KJufG4dFt1IxduJFxLTggkme0oeH5kvyVXHszwTpuLnL+eC54e1yNENkkSUVOSww
OlQW675R4EA8FcxZrSribBDomgDL0mDqxWh/lf6vvlxrwiOLMJSLfpLo2HWl0FWc
wef2o5VBc6+kD/T+hhv7obYDyCOMCOjCZtUvP+IRw72B7QMy/rdFln3HrF4AX/U5
YLwAdKaF09rTYxWiq1x92L5Z7z4gpn071cp2+zFBlZIgyRYRvEd3QJKeBfDqq0XM
hcEbRgkyPFIp6n8+mGmmdb38M2JZulq91U7aRh1boNCZvvHmrnY2HotoSiThhULb
VlvVF+3+fdlPfGzt3wIDAQABozIwMDAPBgNVHRMBAf8EBTADAQH/MB0GA1UdDgQW
BBR62ir+2lomUOLxBEES1a1z42mjRzANBgkqhkiG9w0BAQsFAAOCAQEAcSHtcwsr
F0APIRKM4oH3nhxyi76WzeZIt0rFGm9rzDpcwaKsQGtba+YYFgpjFOr9fsDREYY7
cgnNaW0BMsLT6p+UpVMaZ6Xm3E7Ju/MpHXuJy1HDLJVXjgDi/banW0lZyfDGIugv
ltHNqmm6gSrzTpolr/n2PAwb3Kqxkj4tq7zCnFVoK/uST1gtWnTPp4bbqezepqND
wDmbulALD+LQT14CAzfC01uL8byxrwy26eEQk2u67fv2joRYfdOUfNA8JA/m5AFv
IKCIJhwvAfidQrL3jFgps6UnwAkqKJILBuhsLYCZJsnTqaxSdSiAHblfrLMzY9W4
f9oLbU4vwfIcgQ==
-----END CERTIFICATE-----""";

    public static String getVeraCert() {
        return distSignRootCA;
    }
}
